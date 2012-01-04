package com.jeroensteenbeeke.bk.dbdump;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.text.json.JsonWriteBeanVisitor;
import com.avaje.ebean.text.json.JsonWriter;
import com.google.common.base.Predicate;

public class ForeignKeyVisitor implements JsonWriteBeanVisitor<Object> {
	public class IsFetchTypeLazy implements Predicate<Field> {

		@Override
		public boolean apply(Field field) {
			ManyToOne m1 = field.getAnnotation(ManyToOne.class);

			return m1 != null && m1.fetch() == FetchType.LAZY;
		}

	}

	private static final Logger log = Logger.getLogger("Minecraft");

	public class HasAnnotation implements Predicate<Field> {

		private final Class<? extends Annotation> annotation;

		public HasAnnotation(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

		@Override
		public boolean apply(Field field) {
			return field.isAnnotationPresent(annotation);
		}

	}

	private final EbeanServer server;

	public ForeignKeyVisitor(EbeanServer server) {
		this.server = server;
	}

	@Override
	public void visit(Object obj, JsonWriter writer) {
		Class<? extends Object> objClass = obj.getClass();
		if (objClass.getName().contains("$")) {
			// Proxy
			objClass = objClass.getSuperclass();
		}

		writeIds(objClass, obj, getManyToOneFields(objClass), writer);

	}

	private void writeIds(Class<? extends Object> objClass, Object obj,
			List<Field> fields, JsonWriter writer) {
		for (Field f : fields) {
			try {
				List<Field> ids = newArrayList(filter(newArrayList(f.getType()
						.getDeclaredFields()), new HasAnnotation(Id.class)));
				if (ids.size() == 1) {
					boolean fPrivate = Modifier.isPrivate(f.getModifiers());

					if (fPrivate)
						f.setAccessible(true);

					Object ref = determineGetter(objClass, f).invoke(obj);

					Field target = ids.get(0);
					Method getter = determineGetter(f.getType(), target);

					boolean targetPrivate = Modifier.isPrivate(target
							.getModifiers());

					if (targetPrivate)
						target.setAccessible(true);

					Object value = getter.invoke(ref);

					if (value != null) {
						writer.appendKeyValue(f.getName(),
								"\"" + value.toString() + "\"");
					}

					if (fPrivate)
						f.setAccessible(false);

					if (targetPrivate)
						target.setAccessible(false);
				}
			} catch (IllegalArgumentException e) {
				log.severe("Illegal Argument exception writing foreign keys: "
						+ e.getMessage());
			} catch (IllegalAccessException e) {
				log.severe("Illegal access exception writing foreign keys: "
						+ e.getMessage());
			} catch (SecurityException e) {
				log.severe("Security exception writing foreign keys: "
						+ e.getMessage());
			} catch (NoSuchMethodException e) {
				log.severe("No getter writing foreign keys: " + e.getMessage());
			} catch (InvocationTargetException e) {
				log.severe("Invocation target exception writing foreign keys: "
						+ e.getMessage());
			}
		}

	}

	private Method determineGetter(Class<?> cl, Field field)
			throws NoSuchMethodException {
		return cl.getMethod(String.format("get%C%s", field.getName().charAt(0),
				field.getName().substring(1)));
	}

	private List<Field> getManyToOneFields(Class<?> objClass) {
		Field[] declaredFields = objClass.getDeclaredFields();
		List<Field> fields = newArrayList(filter(
				filterByAnnotation(ManyToOne.class,
						newArrayList(declaredFields)), new IsFetchTypeLazy()));

		return fields;
	}

	private List<Field> filterByAnnotation(
			Class<? extends Annotation> annotation, List<Field> fields) {
		return newArrayList(filter(fields, new HasAnnotation(annotation)));
	}
}
