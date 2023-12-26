import { h, ref, onBeforeUnmount } from 'vue';

export default defineNuxtPlugin((defineConfig) => {
    const events = ref({});

    // 创建事件监听器
    const on = (eventName, callback) => {
        if (!events.value[eventName]) {
            events.value[eventName] = [];
        }
        events.value[eventName].push(callback);
    };

    // 触发事件
    const emit = (eventName, data) => {
        if (events.value[eventName]) {
            events.value[eventName].forEach((callback) => callback(data));
        }
    };

    // 移除事件监听器
    const off = (eventName, callback) => {
        if (events.value[eventName]) {
            events.value[eventName] = events.value[eventName].filter(
                (cb) => cb !== callback
            );
        }
    };

    // 在Nuxt应用销毁时清理事件总线
    onBeforeUnmount(() => {
        Object.values(events.value).forEach((callbacks) => {
            callbacks.forEach((callback) => off(null, callback));
        });
        events.value = {};
    });

    // 提供 eventBus
    provide('eventBus', {
        on,
        emit,
        off,
    });

});
