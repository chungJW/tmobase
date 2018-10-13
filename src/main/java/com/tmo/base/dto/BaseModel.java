package com.tmo.base.dto;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


@MappedSuperclass
public abstract class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer id;

    @JSONField(format = "yyyy-MM-dd", serialize = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date(System.currentTimeMillis());

    @JSONField(format = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public abstract Integer getId();

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        return id != null ? id.equals(baseModel.id) : baseModel.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * 更新这个model
     * @param sourceBean    新model
     * @return
     */
    public BaseModel update(BaseModel sourceBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = this.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if ((sourceField.get(sourceBean) != null) && !"serialVersionUID".equals(sourceField.getName().toString()) && !"id".equals(sourceField.getName().toString())) {
                    targetField.set(this, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Map toMap() {
        return toMap(false);
    }

    public Map toMap(String... contain) {
        return toMap(false, contain);
    }

    //contains 如果要包含userId，需要填 id
    public Map toMap(boolean containNull, String... contain) {
        List contains = Arrays.asList(contain);
        Class targetBeanClass = this.getClass();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        Field[] superFields = targetBeanClass.getSuperclass().getDeclaredFields();
        Field[] fields = new Field[targetFields.length + superFields.length];
        System.arraycopy(targetFields, 0, fields, 0, targetFields.length);
        System.arraycopy(superFields, 0, fields, targetFields.length, superFields.length);

        targetBeanClass.getName();
        Map map = new HashMap();
        for (int i = 0; i < fields.length; i++) {
            Field targetField = fields[i];
            targetField.setAccessible(true);
            try {

                if (contains.size() == 0 ? true : contains.contains(targetField.getName()) && !"serialVersionUID".equals(targetField.getName().toString())) {
                    if (containNull && targetField.get(this) == null) {
                        map.put(targetField.getName(), "");
                    }
                    if (targetField.get(this) != null) {
                        Object value = targetField.get(this);

                        //转换日期格式
                        JSONField jsonField = targetField.getDeclaredAnnotation(JSONField.class);
                        if (jsonField != null) {
                            String format = jsonField.format();
                            if (format != null && format.length() > 0) {
                                String dateString = new SimpleDateFormat(format).format((Date) value);
                                value = dateString;
                            }
                            if(jsonField.serialize()) {
                                map.put(targetField.getName(), value);
                            }
                        }else{
                            map.put(targetField.getName(), value);
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //如果有重写getId方法，使用getId的JSONField的name作为id
        if(contains.size() == 0 && contains.contains("id")) {
            try {
                Method method = targetBeanClass.getDeclaredMethod("getId");
                JSONField jsonField = method.getDeclaredAnnotation(JSONField.class);
                if (jsonField != null) {
                    String name = jsonField.name();
                    if (name != null && !name.equals("")) {
                        map.put(name, map.get("id"));
                        map.remove("id");
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


        return map;
    }
}
