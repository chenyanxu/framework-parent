Ext.define('kalix.Notify', {
    singleton : true,
    container : null,
    options: {},

    mixins: {
        observable: 'Ext.util.Observable'
    },

    type: {
        error: 'error',
        info: 'info',
        success: 'success',
        warning: 'warning'
    },

    constructor: function(){
        this.mixins.observable.constructor.call(this);
    },

    error: function(message, title, optionsOverride) {
        return this.exec({
            type: this.type.error,
            iconClass: this.getOptions().iconClasses.error,
            message: message,
            optionsOverride: optionsOverride,
            title: title
        });
    },

    info: function(message, title, optionsOverride) {
        return this.exec({
            type: this.type.info,
            iconClass: this.getOptions().iconClasses.info,
            message: message,
            optionsOverride: optionsOverride,
            title: title
        });
    },

    success: function(message, title, optionsOverride) {
        return this.exec({
            type: this.type.success,
            iconClass: this.getOptions().iconClasses.success,
            message: message,
            optionsOverride: optionsOverride,
            title: title
        });
    },

    warning: function(message, title, optionsOverride) {
        return this.exec({
            type: this.type.warning,
            iconClass: this.getOptions().iconClasses.warning,
            message: message,
            optionsOverride: optionsOverride,
            title: title
        });
    },

    clear: function(element) {
        var self = this,
            options = self.getOptions();
        if (!self.container) {
            self.getContainer(options);
        }
        if (!self.clearEx(element, options)) {
            self.clearContainer(options);
        }
    },

    remove: function(element) {
        var self = this,
            options = self.getOptions();

        if (!self.container) {
            self.getContainer(options);
        }
        if (element && $(':focus', element).length === 0) {
            return self.removeNotify(element);
        }
        if (self.container.dom.children.length) {
            self.container.remove();
        }
    },

    clearContainer: function(options) {
        var self = this;
        Ext.each(self.container.dom.children,function(el){
            self.clearEx(Ext.get(el), options);
        });
    },

    clearEx: function(element, options) {
        var self = this;
        if (element) {
            element[options.hideMethod]({
                duration: options.hideDuration,
                easing: options.hideEasing,
                callback: Ext.Function.bind(self.removeNotify,self,[element])
            });
            return true;
        }
        return false;
    },

    getContainer: function(options, create) {
        if (!options) {
            options = this.getOptions();
        }

        return self.container = (Ext.get(options.containerId) || (create && this.createContainer(options)));
    },

    createContainer: function(options) {
        return self.container = (new Ext.Element(document.createElement('div'))).set({
            'id': options.containerId,
            'aria-live': 'polite',
            'role': 'alert'
        }).addCls(options.positionClass).appendTo(options.target);
    },

    getDefaults: function() {
        return {
            tapToDismiss: true,
            defaultClass: 'notify',
            containerId: 'notify-container',
            debug: false,

            showMethod: 'fadeIn', //fadeIn, slideDown, and show are built into jQuery
            showDuration: 300,
            showEasing: 'ease', //swing and linear are built into jQuery
            onShown: undefined,
            hideMethod: 'fadeOut',
            hideDuration: 1000,
            hideEasing: 'ease',
            onHidden: undefined,

            extendedTimeOut: 1000,
            iconClasses: {
                error: 'notify-error',
                info: 'notify-info',
                success: 'notify-success',
                warning: 'notify-warning'
            },
            iconClass: 'notify-info',
            positionClass: 'notify-top-center',//notify-bottom-right
            timeOut: 1000, // Set timeOut and extendedTimeOut to 0 to make it sticky
            titleClass: 'notify-title',
            messageClass: 'notify-message',
            target: Ext.getBody(),
            closeElement: 'button',
            closeText: '&times;',
            newestOnTop: true,
            preventDuplicates: false,
            progressBar: false
        };
    },

    exec: function(map) {
        var self = this,
            options = self.getOptions(),
            iconClass = map.iconClass || options.iconClass;

        var hide = function(override) {
                if (!element.dom){
                    return;
                }
                clearTimeout(progressBar.intervalId);
                return element[options.hideMethod]({
                    duration: options.hideDuration,
                    easing: options.hideEasing,
                    callback: function () {
                        self.removeNotify(element);
                        if (options.onHidden && response.state !== 'hidden') {
                            options.onHidden();
                        }
                        response.state = 'hidden';
                        response.endTime = new Date();
                        self.fireEvent('hide');
                    }
                });
            },
            delayedHide = function () {
                if (options.timeOut > 0 || options.extendedTimeOut > 0) {
                    intervalId = setTimeout(hide, options.extendedTimeOut);
                    progressBar.maxHideTime = parseFloat(options.extendedTimeOut);
                    progressBar.hideEta = new Date().getTime() + progressBar.maxHideTime;
                }
            },
            stickAround = function () {
                clearTimeout(intervalId);
                progressBar.hideEta = 0;
                element.stopAnimation();
                element[options.showMethod]({
                    duration: options.showDuration,
                    easing: options.showEasing
                });
            },
            updateProgress = function () {
                var percentage = ((progressBar.hideEta - (new Date().getTime())) / progressBar.maxHideTime) * 100;
                progressElement.width(percentage + '%');
            };

        if (options.preventDuplicates) {
            if (map.message === previousToast) {
                return;
            } else {
                previousToast = map.message;
            }
        }

        if (typeof (map.optionsOverride) !== 'undefined') {
            options = Ext.merge(options, map.optionsOverride);
            iconClass = map.optionsOverride.iconClass || iconClass;
        }

        self.container = self.getContainer(options, true);
        var intervalId = null,
            element = new Ext.Element(document.createElement('div')),
            titleElement = new Ext.Element(document.createElement('div')),
            messageElement = new Ext.Element(document.createElement('div')),
            progressElement = new Ext.Element(document.createElement('div')),
            closeElement = new Ext.Element(document.createElement(options.closeElement)),
            progressBar = {
                intervalId: null,
                hideEta: null,
                maxHideTime: null
            },
            response = {
                state: 'visible',
                startTime: new Date(),
                options: options,
                map: map
            };

        closeElement.innerHTML = options.closeText;

        if (map.iconClass) {
            element.addCls(options.defaultClass).addCls(iconClass);
        }

        if (map.title) {
            var titleNode = new Ext.Element(document.createTextNode(map.title));
            titleElement.addCls(options.titleClass).appendChild(titleNode);
            element.appendChild(titleElement);
        }

        if (map.message) {
            var textNode = new Ext.Element(document.createTextNode(map.message));
            messageElement.addCls(options.messageClass).appendChild(textNode);
            element.appendChild(messageElement);
        }

        if (options.closeButton) {
            closeElement.addCls('notify-close-button').set({
                'role': 'button'
            });
            element.insertFirst(closeElement);
        }

        if (options.progressBar) {
            progressElement.addClass('notify-progress');
            element.insertFirst(progressElement);
        }

        element.hide();
        if (options.newestOnTop) {
            self.container.insertFirst(element);
        } else {
            self.container.appendChild(element);
        }
        element[options.showMethod]({
            duration: options.showDuration,
            easing: options.showEasing,
            callback: options.onShown
        });

        if (options.timeOut > 0) {
            intervalId = setTimeout(hide, options.timeOut);
            progressBar.maxHideTime = parseFloat(options.timeOut);
            progressBar.hideEta = new Date().getTime() + progressBar.maxHideTime;
            if (options.progressBar) {
                progressBar.intervalId = setInterval(updateProgress, 10);
            }
        }

        element.hover(stickAround, delayedHide);
        if (!options.onclick && options.tapToDismiss) {
            element.on('click',hide);
        }

        if (options.closeButton && closeElement) {
            closeElement.on('click',function (event) {
                if (event.stopPropagation) {
                    event.stopPropagation();
                } else if (event.cancelBubble !== undefined && event.cancelBubble !== true) {
                    event.cancelBubble = true;
                }
                hide(true);
            });
        }

        if (options.onclick) {
            element.on('click',function () {
                options.onclick();
                hide();
            });
        }

        self.fireEvent('show');

        if (options.debug && console) {
            Ext.log({
                level: 'log'
            },response);
        }

        return element;
    },

    getOptions: function() {
        return Ext.merge({}, this.getDefaults(), this.options);
    },

    removeNotify: function(element) {
        var self = this;
        if (!self.container) {
            self.getContainer(options);
        }
        if (element.isVisible()) {
            return;
        }
        element.remove();
        element = null;
        if (self.container.dom.children.length === 0) {
            self.container.remove();
        }
    }
});