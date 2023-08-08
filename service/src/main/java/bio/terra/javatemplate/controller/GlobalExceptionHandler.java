package bio.terra.javatemplate.controller;

import bio.terra.common.exception.AbstractGlobalExceptionHandler;
import bio.terra.common.exception.ErrorReportException;
import bio.terra.javatemplate.model.ErrorReport;
import java.util.List;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler<ErrorReport> {

  @Override
  public ErrorReport generateErrorReport(Throwable ex, HttpStatus statusCode, List<String> causes) {
    // If a bean creation failed due to an error report exception, use that exception
    // to generate the error report. This lets us provide the correct status code and a more useful message.
    //
    // This is specific for BeanCreationException, but it might make sense to always look for an ErrorReportException
    // in the cause chain and use it if found.
    if (ex instanceof BeanCreationException
        && ex.getCause() instanceof BeanInstantiationException
        && ex.getCause().getCause() instanceof ErrorReportException ere) {
      ex = ere;
      statusCode = ere.getStatusCode();
    }
    return new ErrorReport().message(ex.getMessage()).statusCode(statusCode.value());
  }
}
