package fr.byob.bs.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

//http://seamframework.org/Community/SeamCustomValidator

@ValidatorClass(UniqueValidator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Unique {

	String entity();
	
	String field();
	
	String message() default "{validator.unique}";
	
}
