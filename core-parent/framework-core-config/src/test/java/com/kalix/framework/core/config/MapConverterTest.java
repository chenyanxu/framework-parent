package com.kalix.framework.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalix.framework.core.config.internal.MapConverter;
import org.junit.Test;

import java.util.Dictionary;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author markusw
 */
public class MapConverterTest {

    /**
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testConversion() throws Exception {
        final Map<String, Object> map = new ObjectMapper()
                .readValue(
                        "{\"some\": \"test\",\"array\": [1, 2, 3],\"array2\": [{\"a\":\"b\"},{\"c\":\"d\"}],\"object\": {\"key1\": \"value1\",\"key2\": \"value2\"}}",
                        Map.class);
        final Dictionary<String, Object> dict = MapConverter.convert(map);

        assertThat(dict.size(), is(8));
        assertThat((String) dict.get("some"), is("test"));
        assertThat((Integer) dict.get("array.0"), is(1));
        assertThat((String) dict.get("array2.0.a"), is("b"));
        assertThat((String) dict.get("object.key2"), is("value2"));
    }

}
