import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  server: {
    port: 3000,
    strictPort: true,
  },
  build: {
    outDir: 'dist/aem-vite-demo.esmodule',
    brotliSize: false,
    manifest: false,
    rollupOptions: {
      output: [
        {
          assetFileNames: '[ext]/[name][extname]',
          chunkFileNames: 'chunks/[name].[hash].js',
          entryFileNames: 'js/[name].js',
        },
      ],
      input: {
        bundle: 'src/index.ts',
      },
    },
  },
  plugins: [vue()],
});
