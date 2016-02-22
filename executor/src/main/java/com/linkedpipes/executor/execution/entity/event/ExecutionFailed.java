package com.linkedpipes.executor.execution.entity.event;

import com.linkedpipes.etl.executor.api.v1.rdf.StatementWriter;
import com.linkedpipes.etl.executor.api.v1.vocabulary.LINKEDPIPES;
import com.linkedpipes.utils.core.event.boundary.AbstractEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Used to report execution failure - {@link StopExecution} message is automatically emitted after this message by core.
 *
 * @author Škoda Petr
 */
public class ExecutionFailed extends AbstractEvent {

    public String reason;

    public Throwable cause;

    public ExecutionFailed(String reason, Throwable cause, String type, String label, String labelLanguage) {
        super(type, label, labelLanguage);
        this.reason = reason;
        this.cause = cause;
    }

    @Override
    public void write(StatementWriter writer) {
        super.write(writer);
        writer.addString(uri, LINKEDPIPES.EVENTS.HAS_REASON, reason, labelLanguage);
        //
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        writer.addString(uri, LINKEDPIPES.EVENTS.HAS_EXCEPTION, sw.toString(), labelLanguage);
        //
        Throwable rootCause = cause;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        writer.addString(uri, LINKEDPIPES.EVENTS.HAS_ROOT_EXCEPTION_MESSAGE, rootCause.getMessage(), "en");
    }

    public static AbstractEvent executionFailed(String reason) {
        return new ExecutionFailed(reason, null, LINKEDPIPES.EVENTS.EXECUTION_FAILED, "Execution failed.", "en");
    }

    public static AbstractEvent executionFailed(String reason, Throwable cause) {
        return new ExecutionFailed(reason, cause, LINKEDPIPES.EVENTS.EXECUTION_FAILED, "Execution failed.", "en");
    }

    public static AbstractEvent initializationFailed(String reason) {
        return new ExecutionFailed(reason, null, LINKEDPIPES.EVENTS.INITIALIZATION_FAILED,
                "Initialization failed.", "en");
    }

    public static AbstractEvent initializationFailed(String reason, Throwable cause) {
        return new ExecutionFailed(reason, cause, LINKEDPIPES.EVENTS.INITIALIZATION_FAILED,
                "Initialization failed.", "en");
    }

}