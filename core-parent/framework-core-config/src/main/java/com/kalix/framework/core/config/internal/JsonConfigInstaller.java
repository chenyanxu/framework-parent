package com.kalix.framework.core.config.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

import java.io.File;
import java.util.*;

/**
 * @类描述：osgi的初始化类
 * @创建人： sunlingfeng
 * @创建时间：2014/12/17
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class JsonConfigInstaller implements BundleActivator, ArtifactInstaller {

    private static final String JSON_CONFIG_FILE = JsonConfigInstaller.class
            .getName();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ConfigurationAdminTracker tracker;

    private ConfigurationAdmin configurationAdmin;

    @Override
    public void start(final BundleContext context) throws Exception {
        System.out.println("started json config bundle!");
        this.tracker = new ConfigurationAdminTracker(context);
        this.tracker.open();
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        System.out.println("stop json config bundle!");
        if (this.tracker != null) {
            this.tracker.close();
            this.tracker = null;
        }
    }

    @Override
    public void install(File artifact) throws Exception {
        setConfig(artifact);
    }

    @Override
    public void update(File artifact) throws Exception {
        setConfig(artifact);
    }

    @Override
    public void uninstall(File artifact) throws Exception {
        final String pid[] = parsePid(artifact.getName());
        final Configuration config = getConfiguration(toConfigKey(artifact),
                pid[0], pid[1]);
        config.delete();
    }

    @Override
    public boolean canHandle(File artifact) {
        return artifact.getName().endsWith(".json");
    }

    private boolean setConfig(final File file) throws Exception {
        final String pid[] = parsePid(file.getName());
        final Configuration config = getConfiguration(toConfigKey(file), pid[0],
                pid[1]);
        final Dictionary<String, Object> props = config.getProperties();
        @SuppressWarnings("unchecked") final Map<String, Object> newProps = this.objectMapper.readValue(file,
                HashMap.class);
        final Dictionary<String, Object> dict = convert(newProps);
        if (!dict.equals(props)) {
            dict.put(JSON_CONFIG_FILE, toConfigKey(file));
            config.update(dict);
            return true;
        }
        return false;
    }

    private Dictionary<String, Object> convert(final Map<String, Object> map) {
        final Dictionary<String, Object> dict = new Hashtable<String, Object>();
        convert(dict, "", map);
        return dict;
    }

    private void convert(final Dictionary<String, Object> dict,
                         final String base, final Map<String, Object> map) {
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            convert(dict, base, entry.getKey(), entry.getValue());
        }
    }

    private void convert(final Dictionary<String, Object> dict,
                         final String base, final Collection<Object> collection) {
        int key = 0;
        final Iterator<Object> it = collection.iterator();
        while (it.hasNext()) {
            convert(dict, base, String.valueOf(key), it.next());
            key++;
        }
    }

    @SuppressWarnings("unchecked")
    private void convert(final Dictionary<String, Object> dict,
                         final String base, final String key, final Object value) {
        if (isSimpleType(value.getClass())) {
            dict.put(base + key, value);
        } else if (value instanceof Collection) {
            convert(dict, base + key + '.', (Collection<Object>) value);
        } else if (value instanceof Map) {
            convert(dict, base + key + '.', (Map<String, Object>) value);
        } else {
            throw new IllegalArgumentException("key:" + key + ":value:" + value);
        }
    }

    private boolean isSimpleType(final Class<?> type) {
        return type == String.class || type == Integer.class || type == Long.class
                || type == Float.class || type == Double.class || type == Byte.class
                || type == Short.class || type == Character.class
                || type == Boolean.class;
    }

    private String[] parsePid(final String path) {
        String pid = path.substring(0, path.lastIndexOf('.'));
        final int n = pid.indexOf('-');
        if (n > 0) {
            final String factoryPid = pid.substring(n + 1);
            pid = pid.substring(0, n);
            return new String[]{pid, factoryPid};
        } else {
            return new String[]{pid, null};
        }
    }

    private String toConfigKey(final File f) {
        return f.getAbsoluteFile().toURI().toString();
    }

    private Configuration getConfiguration(final String fileName,
                                           final String pid, final String factoryPid) throws Exception {
        final Configuration oldConfiguration = findExistingConfiguration(fileName);
        if (oldConfiguration != null) {
            return oldConfiguration;
        } else if (factoryPid != null) {
            return getConfigurationAdmin().createFactoryConfiguration(pid, null);
        }
        return getConfigurationAdmin().getConfiguration(pid, null);
    }

    private Configuration findExistingConfiguration(final String fileName)
            throws Exception {
        final String filter = "(" + JSON_CONFIG_FILE + '=' + fileName + ")";
        final Configuration[] configurations = getConfigurationAdmin()
                .listConfigurations(filter);
        if (configurations != null && configurations.length > 0) {
            return configurations[0];
        } else {
            return null;
        }
    }

    private ConfigurationAdmin getConfigurationAdmin() {
        return this.configurationAdmin;
    }

    private class ConfigurationAdminTracker extends
            ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> {

        private ServiceRegistration<?> installer;

        /**
         * @param context
         */
        public ConfigurationAdminTracker(final BundleContext context) {
            super(context, ConfigurationAdmin.class, null);
        }

        /**
         * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
         */
        @Override
        public ConfigurationAdmin addingService(
                final ServiceReference<ConfigurationAdmin> reference) {
            final ConfigurationAdmin ca = super.addingService(reference);
            if (JsonConfigInstaller.this.configurationAdmin == null) {
                JsonConfigInstaller.this.configurationAdmin = ca;
                this.installer = this.context.registerService(
                        new String[]{ArtifactInstaller.class.getName()},
                        JsonConfigInstaller.this, null);
            }
            return ca;
        }

        /**
         * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference,
         * java.lang.Object)
         */
        @Override
        public void removedService(
                final ServiceReference<ConfigurationAdmin> reference,
                final ConfigurationAdmin service) {
            if (JsonConfigInstaller.this.configurationAdmin == service) {
                this.installer.unregister();
                JsonConfigInstaller.this.configurationAdmin = null;
            }
            super.removedService(reference, service);
        }

    }
}
