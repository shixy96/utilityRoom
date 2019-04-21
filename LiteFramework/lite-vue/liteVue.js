
class liteVue {
    constructor(options) {
        this.$options = options;

        this.$data = options.data;
        this.observe(this.$data);
        new Compile(options.el, this);

        if (options.created) {
            options.created.call(this);
        }
    }

    /**注册data为响应式 */
    observe(data) {
        if (!data || typeof data !== 'object') {
            return;
        }

        Object.keys(data).forEach(key => {
            this.defineReactive(data, key, data[key]);
            this.proxyData(key);
        })
    }

    /**数据响应化 */
    defineReactive(obj, key, val) {
        this.observe(val);
        const dep = new Dep();
        Object.defineProperty(obj, key, {
            get: function () {
                Dep.target && dep.addDep(Dep.target);
                return val;
            },
            set: function (newVal) {
                if (newVal === val) {
                    return;
                }
                val = newVal;
                dep.notify();
            }
        })
    }

    proxyData(key) {
        Object.defineProperty(this, key, {
            get: function () {
                return this.$data[key];
            },
            set: function (newVal) {
                this.$data[key] = newVal;
            }
        })
    }

}

/**用来管理 Watcher */
class Dep {
    constructor() {
        // 存放若干依赖 (Watcher)
        this.deps = [];
        this.target = null;
    }

    addDep(dep) {
        this.deps.push(dep);
    }

    notify() {
        this.deps.forEach(dep => {
            dep.update();
        })
    }
}

class Watcher {
    constructor(vm, key, callback) {
        this.vm = vm;
        this.key = key;
        this.callback = callback;

        // 将当前 watcher 的实例指定到 Dep 静态属性 target
        Dep.target = this;
        this.vm[this.key]; //触发getter，添加依赖
        Dep.target = null;
    }

    update() {
        this.callback.call(this.vm, this.vm[this.key]);
    }
}