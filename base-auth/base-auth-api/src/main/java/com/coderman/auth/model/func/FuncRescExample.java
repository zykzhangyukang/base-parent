package com.coderman.auth.model.func;

import java.util.ArrayList;
import java.util.List;

public class FuncRescExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FuncRescExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andFuncRescIdIsNull() {
            addCriterion("func_resc_id is null");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdIsNotNull() {
            addCriterion("func_resc_id is not null");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdEqualTo(Integer value) {
            addCriterion("func_resc_id =", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdNotEqualTo(Integer value) {
            addCriterion("func_resc_id <>", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdGreaterThan(Integer value) {
            addCriterion("func_resc_id >", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("func_resc_id >=", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdLessThan(Integer value) {
            addCriterion("func_resc_id <", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdLessThanOrEqualTo(Integer value) {
            addCriterion("func_resc_id <=", value, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdIn(List<Integer> values) {
            addCriterion("func_resc_id in", values, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdNotIn(List<Integer> values) {
            addCriterion("func_resc_id not in", values, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdBetween(Integer value1, Integer value2) {
            addCriterion("func_resc_id between", value1, value2, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncRescIdNotBetween(Integer value1, Integer value2) {
            addCriterion("func_resc_id not between", value1, value2, "funcRescId");
            return (Criteria) this;
        }

        public Criteria andFuncIdIsNull() {
            addCriterion("func_id is null");
            return (Criteria) this;
        }

        public Criteria andFuncIdIsNotNull() {
            addCriterion("func_id is not null");
            return (Criteria) this;
        }

        public Criteria andFuncIdEqualTo(Integer value) {
            addCriterion("func_id =", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdNotEqualTo(Integer value) {
            addCriterion("func_id <>", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdGreaterThan(Integer value) {
            addCriterion("func_id >", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("func_id >=", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdLessThan(Integer value) {
            addCriterion("func_id <", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdLessThanOrEqualTo(Integer value) {
            addCriterion("func_id <=", value, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdIn(List<Integer> values) {
            addCriterion("func_id in", values, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdNotIn(List<Integer> values) {
            addCriterion("func_id not in", values, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdBetween(Integer value1, Integer value2) {
            addCriterion("func_id between", value1, value2, "funcId");
            return (Criteria) this;
        }

        public Criteria andFuncIdNotBetween(Integer value1, Integer value2) {
            addCriterion("func_id not between", value1, value2, "funcId");
            return (Criteria) this;
        }

        public Criteria andRescIdIsNull() {
            addCriterion("resc_id is null");
            return (Criteria) this;
        }

        public Criteria andRescIdIsNotNull() {
            addCriterion("resc_id is not null");
            return (Criteria) this;
        }

        public Criteria andRescIdEqualTo(Integer value) {
            addCriterion("resc_id =", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdNotEqualTo(Integer value) {
            addCriterion("resc_id <>", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdGreaterThan(Integer value) {
            addCriterion("resc_id >", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("resc_id >=", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdLessThan(Integer value) {
            addCriterion("resc_id <", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdLessThanOrEqualTo(Integer value) {
            addCriterion("resc_id <=", value, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdIn(List<Integer> values) {
            addCriterion("resc_id in", values, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdNotIn(List<Integer> values) {
            addCriterion("resc_id not in", values, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdBetween(Integer value1, Integer value2) {
            addCriterion("resc_id between", value1, value2, "rescId");
            return (Criteria) this;
        }

        public Criteria andRescIdNotBetween(Integer value1, Integer value2) {
            addCriterion("resc_id not between", value1, value2, "rescId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}