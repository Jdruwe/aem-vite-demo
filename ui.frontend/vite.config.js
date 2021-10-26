import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  server: {
    port: 3000,
    strictPort: true,
  },
  build: {
    brotliSize: false,
    manifest: true,
    rollupOptions: {
      output: {
        assetFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule/resources/[ext]/[name].[hash][extname]',
        chunkFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule/resources/chunks/[name].[hash].js',
        entryFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule/resources/js/[name].[hash].js',
      },
      input: {
        app: 'src/main.ts',
      },
    },
  },
  plugins: [vue()],
});
