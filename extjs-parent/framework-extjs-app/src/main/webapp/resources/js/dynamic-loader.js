var DynamicLoading = {
    css: function (path) {
        if (!path || path.length === 0) {
            throw new Error('argument "path" is required !');
        }

        var head = document.getElementsByTagName('head')[0];
        var link = document.createElement('link');
        link.href = path;
        link.rel = 'stylesheet';
        link.type = 'text/css';
        head.appendChild(link);
    },
    js: function (path) {
        if (!path || path.length === 0) {
            throw new Error('argument "path" is required !');
        }

        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');

        script.src = path;
        script.type = 'text/javascript';
        head.appendChild(script);
    },
    favicon: function (path) {
        if (!path || path.length === 0) {
            throw new Error('argument "path" is required !');
        }

        var head = document.getElementsByTagName('head')[0];
        var link = document.createElement('link');
        link.href = path;
        link.rel = 'shortcut icon';
        link.type = 'image/x-icon';
        link.media='screen';
        head.appendChild(link);
    }
};

