import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    port: 3001,
    strictPort: true,
  },
  build: {
    outDir: 'dist/aem-vite-demo.esmodule.other',
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
        other: 'src/other.ts',
      },
    },
  },
});
