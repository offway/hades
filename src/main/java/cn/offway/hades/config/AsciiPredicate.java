package cn.offway.hades.config;


import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.predicate.AbstractSimplePredicate;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models a <tt>BETWEEN</tt> {@link javax.persistence.criteria.Predicate}.
 *
 * @author Steve Ebersole
 */
public class AsciiPredicate<Y>
        extends AbstractSimplePredicate
        implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Expression<? extends Y> expression;
    private final Expression<? extends Y> object;

    public AsciiPredicate(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<? extends Y> expression,
            Y object) {
        this(
                criteriaBuilder,
                expression,
                criteriaBuilder.literal(object)
        );
    }

    public AsciiPredicate(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<? extends Y> expression,
            Expression<? extends Y> object) {
        super(criteriaBuilder);
        this.expression = expression;
        this.object = object;
    }

    public Expression<? extends Y> getExpression() {
        return expression;
    }

    public Expression<? extends Y> getObject() {
        return object;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(getExpression(), registry);
        Helper.possibleParameter(getObject(), registry);
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return "ascii("
                + ((Renderable) getExpression()).render(renderingContext)
                + ")"
                + " > 127";
    }
}
