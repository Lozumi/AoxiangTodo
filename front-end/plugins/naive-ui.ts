import { setup } from '@css-render/vue3-ssr'
import { defineNuxtPlugin } from '#app'

export default defineNuxtPlugin((nuxtApp) => {
    if (process.server) {
        const { collect } = setup(nuxtApp.vueApp)
        //这个错误可能是因为 TypeScript 不确定 nuxtApp.ssrContext 是否为 undefined
        // 因此我们需要明确告诉 TypeScript nuxtApp.ssrContext 不会为 undefined。
        //下行将“？”改为了“！”，需要确保 nuxtApp.ssrContext 的确不为 undefined
        const originalRenderMeta = nuxtApp.ssrContext!.renderMeta;
        //下行代码将在”||“前加了一个”1"
        nuxtApp.ssrContext = nuxtApp.ssrContext !|| {};
        nuxtApp.ssrContext.renderMeta = () => {
            if (!originalRenderMeta) {
                return {
                    headTags: collect()
                }
            }
            const originalMeta = originalRenderMeta()
            if ('then' in originalMeta) {
                //这个错误是因为 TypeScript 无法确定 resolvedOriginalMeta 的类型。
                // 为了解决这个问题，你可以明确指定 resolvedOriginalMeta 的类型，或者使用类型断言
                //下行将 resolvedOriginalMeta 显式地标记为 any 类型。
                // 这种方法可以解决 TypeScript 报错，但要注意使用类型断言时需要确保你了解代码的运行时行为，以防止类型不匹配导致的问题。
                return originalMeta.then((resolvedOriginalMeta:any) => {
                    return {
                        ...resolvedOriginalMeta,
                        headTags: resolvedOriginalMeta['headTags'] + collect()
                    }
                })
            } else {
                return {
                    ...originalMeta,
                    headTags: originalMeta['headTags'] + collect()
                }
            }
        }
    }
})
