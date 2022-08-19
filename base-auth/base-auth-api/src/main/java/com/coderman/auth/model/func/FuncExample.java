package com.coderman.auth.model.func;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuncExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FuncExample() {
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

        public Criteria andFuncNameIsNull() {
            addCriterion("func_name is null");
            return (Criteria) this;
        }

        public Criteria andFuncNameIsNotNull() {
            addCriterion("func_name is not null");
            return (Criteria) this;
        }

        public Criteria andFuncNameEqualTo(String value) {
            addCriterion("func_name =", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameNotEqualTo(String value) {
            addCriterion("func_name <>", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameGreaterThan(String value) {
            addCriterion("func_name >", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameGreaterThanOrEqualTo(String value) {
            addCriterion("func_name >=", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameLessThan(String value) {
            addCriterion("func_name <", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameLessThanOrEqualTo(String value) {
            addCriterion("func_name <=", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameLike(String value) {
            addCriterion("func_name like", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameNotLike(String value) {
            addCriterion("func_name not like", value, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameIn(List<String> values) {
            addCriterion("func_name in", values, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameNotIn(List<String> values) {
            addCriterion("func_name not in", values, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameBetween(String value1, String value2) {
            addCriterion("func_name between", value1, value2, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncNameNotBetween(String value1, String value2) {
            addCriterion("func_name not between", value1, value2, "funcName");
            return (Criteria) this;
        }

        public Criteria andFuncKeyIsNull() {
            addCriterion("func_key is null");
            return (Criteria) this;
        }

        public Criteria andFuncKeyIsNotNull() {
            addCriterion("func_key is not null");
            return (Criteria) this;
        }

        public Criteria andFuncKeyEqualTo(String value) {
            addCriterion("func_key =", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyNotEqualTo(String value) {
            addCriterion("func_key <>", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyGreaterThan(String value) {
            addCriterion("func_key >", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyGreaterThanOrEqualTo(String value) {
            addCriterion("func_key >=", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyLessThan(String value) {
            addCriterion("func_key <", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyLessThanOrEqualTo(String value) {
            addCriterion("func_key <=", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyLike(String value) {
            addCriterion("func_key like", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyNotLike(String value) {
            addCriterion("func_key not like", value, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyIn(List<String> values) {
            addCriterion("func_key in", values, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyNotIn(List<String> values) {
            addCriterion("func_key not in", values, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyBetween(String value1, String value2) {
            addCriterion("func_key between", value1, value2, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncKeyNotBetween(String value1, String value2) {
            addCriterion("func_key not between", value1, value2, "funcKey");
            return (Criteria) this;
        }

        public Criteria andFuncTypeIsNull() {
            addCriterion("func_type is null");
            return (Criteria) this;
        }

        public Criteria andFuncTypeIsNotNull() {
            addCriterion("func_type is not null");
            return (Criteria) this;
        }

        public Criteria andFuncTypeEqualTo(String value) {
            addCriterion("func_type =", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeNotEqualTo(String value) {
            addCriterion("func_type <>", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeGreaterThan(String value) {
            addCriterion("func_type >", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeGreaterThanOrEqualTo(String value) {
            addCriterion("func_type >=", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeLessThan(String value) {
            addCriterion("func_type <", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeLessThanOrEqualTo(String value) {
            addCriterion("func_type <=", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeLike(String value) {
            addCriterion("func_type like", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeNotLike(String value) {
            addCriterion("func_type not like", value, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeIn(List<String> values) {
            addCriterion("func_type in", values, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeNotIn(List<String> values) {
            addCriterion("func_type not in", values, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeBetween(String value1, String value2) {
            addCriterion("func_type between", value1, value2, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncTypeNotBetween(String value1, String value2) {
            addCriterion("func_type not between", value1, value2, "funcType");
            return (Criteria) this;
        }

        public Criteria andFuncIconIsNull() {
            addCriterion("func_icon is null");
            return (Criteria) this;
        }

        public Criteria andFuncIconIsNotNull() {
            addCriterion("func_icon is not null");
            return (Criteria) this;
        }

        public Criteria andFuncIconEqualTo(String value) {
            addCriterion("func_icon =", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconNotEqualTo(String value) {
            addCriterion("func_icon <>", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconGreaterThan(String value) {
            addCriterion("func_icon >", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconGreaterThanOrEqualTo(String value) {
            addCriterion("func_icon >=", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconLessThan(String value) {
            addCriterion("func_icon <", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconLessThanOrEqualTo(String value) {
            addCriterion("func_icon <=", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconLike(String value) {
            addCriterion("func_icon like", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconNotLike(String value) {
            addCriterion("func_icon not like", value, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconIn(List<String> values) {
            addCriterion("func_icon in", values, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconNotIn(List<String> values) {
            addCriterion("func_icon not in", values, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconBetween(String value1, String value2) {
            addCriterion("func_icon between", value1, value2, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncIconNotBetween(String value1, String value2) {
            addCriterion("func_icon not between", value1, value2, "funcIcon");
            return (Criteria) this;
        }

        public Criteria andFuncSortIsNull() {
            addCriterion("func_sort is null");
            return (Criteria) this;
        }

        public Criteria andFuncSortIsNotNull() {
            addCriterion("func_sort is not null");
            return (Criteria) this;
        }

        public Criteria andFuncSortEqualTo(Integer value) {
            addCriterion("func_sort =", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortNotEqualTo(Integer value) {
            addCriterion("func_sort <>", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortGreaterThan(Integer value) {
            addCriterion("func_sort >", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortGreaterThanOrEqualTo(Integer value) {
            addCriterion("func_sort >=", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortLessThan(Integer value) {
            addCriterion("func_sort <", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortLessThanOrEqualTo(Integer value) {
            addCriterion("func_sort <=", value, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortIn(List<Integer> values) {
            addCriterion("func_sort in", values, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortNotIn(List<Integer> values) {
            addCriterion("func_sort not in", values, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortBetween(Integer value1, Integer value2) {
            addCriterion("func_sort between", value1, value2, "funcSort");
            return (Criteria) this;
        }

        public Criteria andFuncSortNotBetween(Integer value1, Integer value2) {
            addCriterion("func_sort not between", value1, value2, "funcSort");
            return (Criteria) this;
        }

        public Criteria andDirHideIsNull() {
            addCriterion("dir_hide is null");
            return (Criteria) this;
        }

        public Criteria andDirHideIsNotNull() {
            addCriterion("dir_hide is not null");
            return (Criteria) this;
        }

        public Criteria andDirHideEqualTo(Boolean value) {
            addCriterion("dir_hide =", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideNotEqualTo(Boolean value) {
            addCriterion("dir_hide <>", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideGreaterThan(Boolean value) {
            addCriterion("dir_hide >", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideGreaterThanOrEqualTo(Boolean value) {
            addCriterion("dir_hide >=", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideLessThan(Boolean value) {
            addCriterion("dir_hide <", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideLessThanOrEqualTo(Boolean value) {
            addCriterion("dir_hide <=", value, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideIn(List<Boolean> values) {
            addCriterion("dir_hide in", values, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideNotIn(List<Boolean> values) {
            addCriterion("dir_hide not in", values, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideBetween(Boolean value1, Boolean value2) {
            addCriterion("dir_hide between", value1, value2, "dirHide");
            return (Criteria) this;
        }

        public Criteria andDirHideNotBetween(Boolean value1, Boolean value2) {
            addCriterion("dir_hide not between", value1, value2, "dirHide");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Integer value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Integer value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Integer value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Integer value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Integer> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Integer> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Integer value1, Integer value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
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