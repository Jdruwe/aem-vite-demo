import {defineConfig} from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
    build: {
        brotliSize: false,
        manifest: false,
        rollupOptions: {
            output: {
                assetFileNames: 'aem-vite-demo.esmodule/[ext]/[name][extname]',
                chunkFileNames: 'aem-vite-demo.esmodule/chunks/[name].[hash].js',
                entryFileNames: 'aem-vite-demo.esmodule/js/[name].js',
            },
            input: {
                bundle: 'src/index.ts',
            },
        },
    },
    plugins: [vue()]
});
