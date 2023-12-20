// import { defineNuxtConfig } from 'nuxt'
//
// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  build: {
    transpile:
        process.env.NODE_ENV === 'production'
            ? [
              'naive-ui',
              'vueuc',
              '@css-render/vue3-ssr',
              '@juggle/resize-observer',
                'date-fns'
            ]
            : ['@juggle/resize-observer']
  },
  vite: {
    optimizeDeps: {
      include:
          process.env.NODE_ENV === 'development'
              ? ['naive-ui', 'vueuc', 'date-fns-tz/esm/formatInTimeZone']
              : []
    }
  }
})
