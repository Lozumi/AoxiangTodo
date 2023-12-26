// import { defineNuxtConfig } from 'nuxt'
//
// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    builder: undefined,
    $development: undefined,
        $env: undefined,
        $meta: undefined, $production: undefined, $test: undefined, appConfig: undefined, build: {
            transpile:
                process.env.NODE_ENV === 'production'
                    ? [
                        'naive-ui',
                        'vueuc',
                        '@css-render/vue3-ssr',
                        '@juggle/resize-observer',
                        'date-fns-tz',
                        '@nuxtjs/tailwindcss',

                    ]
                    : ['@juggle/resize-observer']
        },
        devtools: {enabled: true},

    modules: [
        'vuetify-nuxt-module',
        'nuxt-socket-io',
        '@nuxtjs/composition-api',
        '@nuxtjs/axios',
        '@nuxtjs/proxy',
    ],
        io: {
            // module options
            sockets: [{
                name: 'main',
                url: 'http://localhost:3000'
            }]
        },

        telemetry: undefined,
        vite: {
            optimizeDeps: {
                include:
                    process.env.NODE_ENV === 'development'
                        // ? ['naive-ui', 'vueuc', 'date-fns-tz/esm/formatInTimeZone']
                        ? ['naive-ui', 'vueuc']
                        : []
            }
        },
        vuetify: {
            moduleOptions: {
                /* module specific options */
            },
            vuetifyOptions: {
                /* vuetify options */
            }
        }

    }
)
