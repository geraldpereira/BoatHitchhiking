package fr.byob.bs.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@ValidatorClass(MailConfirmValidator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface MailConfirm {
	String message() default "{validator.mailConfirm}";
}
