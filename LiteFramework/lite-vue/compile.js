class Compile {
    /**
     * @param {String} el The querySelector
     * @param {Object} vm The instance
     */
    constructor(el, vm) {
        this.$el = document.querySelector(el);
        if (!this.$el) {
            return;
        }

        this.$vm = vm;
        this.$fragment = this.node2Fragment(this.$el);
        this.compile(this.$fragment);

        this.$el.appendChild(this.$fragment);
    }

    /**
     * @param {Node} el 
     */
    node2Fragment(el) {
        const frag = document.createDocumentFragment();
        let child;
        while (child = el.firstChild) {
            frag.appendChild(child);
        }
        return frag;
    }

    //编译过程
    /**
     * @param {DocumentFragment} el
     */
    compile(el) {
        const childNodes = el.childNodes;
        Array.from(childNodes).forEach(node => {
            if (this.isElement(node)) {
                // console.log('编译元素' + node.nodeName);
                // 查找 k-, @, :
                const nodeAttrs = node.attributes;
                Array.from(nodeAttrs).forEach((attr) => {
                    const attrName = attr.name;
                    const exp = attr.value;
                    if (this.isDirective(attrName)) {
                        //k-
                        const dir = attrName.substring(2);
                        this[dir] && this[dir](node, this.$vm, exp);
                    }
                    if (this.isEvent(attrName)) {
                        const dir = attrName.substring(1); //@click
                        this.eventHandler(node, this.$vm, exp, dir);

                    }
                })
            } else if (this.isInterpolation(node)) {
                this.compileText(node);
            }

            //递归子节点
            if (node.childNodes && node.childNodes.length > 0) {
                this.compile(node);
            }
        })
    }

    /**
     * @param {Node} node 
     */
    compileText(node) {
        this.update(node, this.$vm, RegExp.$1, 'text');
    }

    /**
     * 更新函数
     * @param {Node} node 节点
     * @param {Object} vm 实例
     * @param {Object} exp 表达式
     * @param {Object} dir 指令
     */
    update(node, vm, exp, dir) {
        // 初始化
        const updaterFn = this[dir + 'Updater'];
        updaterFn && updaterFn(node, vm[exp]);

        //依赖收集
        new Watcher(vm, exp, function (value) {
            updaterFn && updaterFn(node, value);
        })
    }

    text(node, vm, exp) {
        this.update(node, vm, exp, 'text');
    }

    model(node, vm, exp) {
        //指定 input 的 value 属性
        this.update(node, vm, exp, 'model');

        //视图对模型的响应
        node.addEventListener('input', e => {
            vm[exp] = e.target.value;
        })
    }

    html(node, vm, exp) {
        this.update(node, vm, exp, 'html');
    }

    modelUpdater(node, value) {
        node.value = value;
    }

    textUpdater(node, value) {
        node.textContent = value;
    }

    htmlUpdater(node, value) {
        node.innerHTML = value;
    }

    eventHandler(node, vm, exp, dir) {
        let fn = vm.$options.methods && vm.$options.methods[exp];
        if (dir && fn) {
            node.addEventListener(dir, fn.bind(vm));
        }
    }

    //https://developer.mozilla.org/zh-CN/docs/Web/API/Node
    /**
     * @param {Node} node 
     */
    isElement(node) {
        return node.nodeType === 1;
    }

    /**
     * @param {Node} node 
     */
    isInterpolation(node) {
        return node.nodeType === 3 && /\{\{\s*(\S*)\s*\}\}/.test(node.textContent);
    }

    isDirective(attrName) {
        return attrName.indexOf('k-') == 0;
    }

    isEvent(attrName) {
        return attrName.indexOf('@') == 0;
    }

}