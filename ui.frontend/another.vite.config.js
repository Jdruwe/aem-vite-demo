import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  server: {
    port: 3001,
    strictPort: true,
  },
  build: {
    outDir: 'dist/clientlib-esmodule-another',
    brotliSize: false,
    manifest: true,
    rollupOptions: {
      output: {
        assetFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule-another/resources/[ext]/[name].[hash][extname]',
        chunkFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule-another/resources/chunks/[name].[hash].js',
        entryFileNames:
          'etc.clientlibs/aem-vite-demo/clientlibs/clientlib-esmodule-another/resources/js/[name].[hash].js',
      },
      input: {
        app: 'src/another.ts',
      },
    },
  },
  plugins: [vue()],
});
