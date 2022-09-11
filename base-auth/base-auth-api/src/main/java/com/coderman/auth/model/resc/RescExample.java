package com.coderman.auth.model.resc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RescExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RescExample() {
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

        public Criteria andRescNameIsNull() {
            addCriterion("resc_name is null");
            return (Criteria) this;
        }

        public Criteria andRescNameIsNotNull() {
            addCriterion("resc_name is not null");
            return (Criteria) this;
        }

        public Criteria andRescNameEqualTo(String value) {
            addCriterion("resc_name =", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameNotEqualTo(String value) {
            addCriterion("resc_name <>", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameGreaterThan(String value) {
            addCriterion("resc_name >", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameGreaterThanOrEqualTo(String value) {
            addCriterion("resc_name >=", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameLessThan(String value) {
            addCriterion("resc_name <", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameLessThanOrEqualTo(String value) {
            addCriterion("resc_name <=", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameLike(String value) {
            addCriterion("resc_name like", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameNotLike(String value) {
            addCriterion("resc_name not like", value, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameIn(List<String> values) {
            addCriterion("resc_name in", values, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameNotIn(List<String> values) {
            addCriterion("resc_name not in", values, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameBetween(String value1, String value2) {
            addCriterion("resc_name between", value1, value2, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescNameNotBetween(String value1, String value2) {
            addCriterion("resc_name not between", value1, value2, "rescName");
            return (Criteria) this;
        }

        public Criteria andRescUrlIsNull() {
            addCriterion("resc_url is null");
            return (Criteria) this;
        }

        public Criteria andRescUrlIsNotNull() {
            addCriterion("resc_url is not null");
            return (Criteria) this;
        }

        public Criteria andRescUrlEqualTo(String value) {
            addCriterion("resc_url =", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlNotEqualTo(String value) {
            addCriterion("resc_url <>", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlGreaterThan(String value) {
            addCriterion("resc_url >", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlGreaterThanOrEqualTo(String value) {
            addCriterion("resc_url >=", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlLessThan(String value) {
            addCriterion("resc_url <", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlLessThanOrEqualTo(String value) {
            addCriterion("resc_url <=", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlLike(String value) {
            addCriterion("resc_url like", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlNotLike(String value) {
            addCriterion("resc_url not like", value, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlIn(List<String> values) {
            addCriterion("resc_url in", values, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlNotIn(List<String> values) {
            addCriterion("resc_url not in", values, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlBetween(String value1, String value2) {
            addCriterion("resc_url between", value1, value2, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescUrlNotBetween(String value1, String value2) {
            addCriterion("resc_url not between", value1, value2, "rescUrl");
            return (Criteria) this;
        }

        public Criteria andRescDomainIsNull() {
            addCriterion("resc_domain is null");
            return (Criteria) this;
        }

        public Criteria andRescDomainIsNotNull() {
            addCriterion("resc_domain is not null");
            return (Criteria) this;
        }

        public Criteria andRescDomainEqualTo(String value) {
            addCriterion("resc_domain =", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainNotEqualTo(String value) {
            addCriterion("resc_domain <>", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainGreaterThan(String value) {
            addCriterion("resc_domain >", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainGreaterThanOrEqualTo(String value) {
            addCriterion("resc_domain >=", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainLessThan(String value) {
            addCriterion("resc_domain <", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainLessThanOrEqualTo(String value) {
            addCriterion("resc_domain <=", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainLike(String value) {
            addCriterion("resc_domain like", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainNotLike(String value) {
            addCriterion("resc_domain not like", value, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainIn(List<String> values) {
            addCriterion("resc_domain in", values, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainNotIn(List<String> values) {
            addCriterion("resc_domain not in", values, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainBetween(String value1, String value2) {
            addCriterion("resc_domain between", value1, value2, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andRescDomainNotBetween(String value1, String value2) {
            addCriterion("resc_domain not between", value1, value2, "rescDomain");
            return (Criteria) this;
        }

        public Criteria andMethodTypeIsNull() {
            addCriterion("method_type is null");
            return (Criteria) this;
        }

        public Criteria andMethodTypeIsNotNull() {
            addCriterion("method_type is not null");
            return (Criteria) this;
        }

        public Criteria andMethodTypeEqualTo(String value) {
            addCriterion("method_type =", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeNotEqualTo(String value) {
            addCriterion("method_type <>", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeGreaterThan(String value) {
            addCriterion("method_type >", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeGreaterThanOrEqualTo(String value) {
            addCriterion("method_type >=", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeLessThan(String value) {
            addCriterion("method_type <", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeLessThanOrEqualTo(String value) {
            addCriterion("method_type <=", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeLike(String value) {
            addCriterion("method_type like", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeNotLike(String value) {
            addCriterion("method_type not like", value, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeIn(List<String> values) {
            addCriterion("method_type in", values, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeNotIn(List<String> values) {
            addCriterion("method_type not in", values, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeBetween(String value1, String value2) {
            addCriterion("method_type between", value1, value2, "methodType");
            return (Criteria) this;
        }

        public Criteria andMethodTypeNotBetween(String value1, String value2) {
            addCriterion("method_type not between", value1, value2, "methodType");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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