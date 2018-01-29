package com.kalix.framework.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @类描述：redis 序列化应用类
 * @创建人：sunlf
 * @创建时间：2014-07-01 下午3:47
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class SerializeUtil {
    public static byte[] serialize(Object object) {
        ByteArrayOutputStream baos;
        ObjectOutputStream oos;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public static String serializeJson(Object object) {

        Gson mapper = new Gson();
        try {
            return mapper.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static <T> T unserializeJson(String json, Class cls) {

        Gson mapper = new Gson();

        try {
            return (T) mapper.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON Unserialize Error");
        }
    }

    /**
     * example:
     * Type type = new TypeToken<ArrayList<String>>() {}.getType();
     * orgList = new Gson().fromJson(rtnOrgStr, type);
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T unserializeJson(String json, Type type) {

        Gson mapper = new Gson();

        try {
            return (T) mapper.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON Unserialize Error");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T unserialize(byte[] bytes) {

        ByteArrayInputStream bais;
        if (null != bytes) {
            try {
                // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais) {
                    Set<ClassLoader> lhs = new LinkedHashSet<ClassLoader>();

                    {
                        // Keep a set if discovered class loaders
                        lhs.add(getClass().getClassLoader());
                    }

                    @Override
                    protected Class<?> resolveClass(ObjectStreamClass desc)
                            throws ClassNotFoundException, IOException {

                        for (ClassLoader cl : lhs)
                            try {
                                Class<?> c = cl.loadClass(desc.getName());

                                // we found the class, so we can use its class loader,
                                // it is in the proper class space  if the uses constraints
                                // are set properly (and you're using bnd so you should be ok)

                                lhs.add(c.getClassLoader());

                                // The paranoid among us would check
                                // the serial uuid here ...
                                // long uuid = desc.getSerialVersionUID();
                                // Field field = c.getField("serialVersionUID");
                                // assert uuid == field.get(null)

                                return c;
                            } catch (Exception e) {
                                // Ignore
                            }

                        // Fallback (for void and primitives)
                        return super.resolveClass(desc);
                    }
                };

                Object o = ois.readObject();
                return (T) o;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Map<String, String> json2Map(String json) {
        Gson gson = new Gson();

        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        if (map == null) {
            map = new HashMap<>();
        }

        return map;
    }

    /**
     * 解析复杂的json对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }
}
