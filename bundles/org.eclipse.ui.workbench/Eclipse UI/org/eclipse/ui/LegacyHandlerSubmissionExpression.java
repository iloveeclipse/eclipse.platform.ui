/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.util.Util;

/**
 * <p>
 * An expression encapsulating all of the information from legacy handler
 * submissions.
 * </p>
 * 
 * @since 3.1
 */
public final class LegacyHandlerSubmissionExpression extends Expression {

	/**
	 * The constant integer hash code value meaning the hash code has not yet
	 * been computed.
	 */
	private static final int HASH_CODE_NOT_COMPUTED = -1;

	/**
	 * A factor for computing the hash code for all schemes.
	 */
	private static final int HASH_FACTOR = 89;

	/**
	 * The seed for the hash code for all schemes.
	 */
	private static final int HASH_INITIAL = LegacyHandlerSubmissionExpression.class
			.getName().hashCode();

	/**
	 * The identifier for the part that must be active for this expression to
	 * evaluate to <code>true</code>. If this value is <code>null</code>,
	 * then any part may be active.
	 */
	private final String activePartId;

	/**
	 * The shell that must be active for this expression to evaluate to
	 * <code>true</code>. If this value is <code>null</code>, then any
	 * shell may be active.
	 */
	private final Shell activeShell;

	/**
	 * The site that must be active for this expression to evaluate to
	 * <code>true</code>. If this value is <code>null</code>, then any
	 * site may be active.
	 */
	private final IWorkbenchPartSite activeSite;

	/**
	 * The hash code for this object. This value is computed lazily, and marked
	 * as invalid when one of the values on which it is based changes.
	 */
	private transient int hashCode = HASH_CODE_NOT_COMPUTED;

	/**
	 * Constructs a new instance of
	 * <code>LegacyHandlerSubmissionExpression</code>
	 * 
	 * @param activePartId
	 *            The part identifier to match with the active part;
	 *            <code>null</code> if it will match any active part.
	 * @param activeShell
	 *            The shell to match with the active shell; <code>null</code>
	 *            if it will match any active shell.
	 * @param activeSite
	 *            The site to match with the active site; <code>null</code> if
	 *            it will match any active site.
	 */
	public LegacyHandlerSubmissionExpression(final String activePartId,
			final Shell activeShell, final IWorkbenchPartSite activeSite) {

		this.activePartId = activePartId;
		this.activeShell = activeShell;
		this.activeSite = activeSite;
	}

	public final void collectExpressionInfo(final ExpressionInfo info) {
		if (activePartId != null) {
			info.addVariableNameAccess(ISources.ACTIVE_PART_ID_NAME);
		}
		if (activeShell != null) {
			info.addVariableNameAccess(ISources.ACTIVE_SHELL_NAME);
			info
					.addVariableNameAccess(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME);
		}
		if (activeSite != null) {
			info.addVariableNameAccess(ISources.ACTIVE_SITE_NAME);
		}
	}

	public final boolean equals(final Object object) {
		if (object instanceof LegacyHandlerSubmissionExpression) {
			final LegacyHandlerSubmissionExpression that = (LegacyHandlerSubmissionExpression) object;
			return Util.equals(this.activePartId, that.activePartId)
					&& Util.equals(this.activeShell, that.activeShell)
					&& Util.equals(this.activeSite, that.activeSite);
		}

		return false;
	}

	/**
	 * Evaluates this expression. This tests the three conditions against the
	 * current state of the application (as defined by <code>context</code>).
	 * If a condition is <code>null</code>, then it matches any possible
	 * value (i.e., it is not tested at all).
	 * 
	 * @param context
	 *            The context providing the current workbench state; must not be
	 *            <code>null</code>.
	 * @return <code>EvaluationResult.TRUE</code> if the conditions all
	 *         matches; <code>EvaluationResult.FALSE</code> otherwise.
	 */
	public final EvaluationResult evaluate(final IEvaluationContext context) {
		if (activePartId != null) {
			final Object value = context
					.getVariable(ISources.ACTIVE_PART_ID_NAME);
			if (!activePartId.equals(value)) {
				return EvaluationResult.FALSE;
			}
		}

		if (activeShell != null) {
			Object value = context.getVariable(ISources.ACTIVE_SHELL_NAME);
			if (!activeShell.equals(value)) {
				value = context
						.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME);
				if (!activeShell.equals(value)) {
					return EvaluationResult.FALSE;
				}
			}
		}

		if (activeSite != null) {
			final Object value = context.getVariable(ISources.ACTIVE_SITE_NAME);
			if (!activeSite.equals(value)) {
				return EvaluationResult.FALSE;
			}
		}

		return EvaluationResult.TRUE;
	}

	/**
	 * Computes the hash code for this object based on the id.
	 * 
	 * @return The hash code for this object.
	 */
	public final int hashCode() {
		if (hashCode == HASH_CODE_NOT_COMPUTED) {
			hashCode = HASH_INITIAL * HASH_FACTOR + Util.hashCode(activePartId);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(activeShell);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(activeSite);
			if (hashCode == HASH_CODE_NOT_COMPUTED) {
				hashCode++;
			}
		}
		return hashCode;
	}

	public final String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("LegacyHandlerSubmission("); //$NON-NLS-1$
		buffer.append(activeShell);
		buffer.append(',');
		buffer.append(activePartId);
		buffer.append(',');
		buffer.append(activeSite);
		buffer.append(')');
		return buffer.toString();
	}
}
