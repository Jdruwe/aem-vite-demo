import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import aemViteImportRewriter from '@aem-vite/import-rewriter';

export default defineConfig(({command, mode}) => ({
    build: {
        brotliSize: false,
        manifest: false,
        minify: mode === 'development' ? false : 'terser',
        outDir: 'dist',
        sourcemap: command === 'serve' ? 'inline' : false,

        rollupOptions: {
            output: {
                assetFileNames: 'aem-vite-demo.epic/[ext]/[name][extname]',
                chunkFileNames: 'aem-vite-demo.epic/chunks/[name].[hash].js',
                entryFileNames: 'aem-vite-demo.epic/js/[name].js',
            },
            input: {
                bundle: 'src/main.ts',
            },
        },
    },

    plugins: [
        vue(),
        {
            ...aemViteImportRewriter({
                publicPath: '/etc.clientlibs/aem-vite-demo/clientlibs/',
            }),

            apply: 'build',
            enforce: 'pre',
        },
    ]
}));