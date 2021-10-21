import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import aemViteImportRewriter from '@aem-vite/import-rewriter';

export default defineConfig(({ command, mode }) => ({
  build: {
    brotliSize: false,
    manifest: true,
    minify: mode === 'development' ? false : 'terser',
    outDir: 'dist',
    sourcemap: command === 'serve' ? 'inline' : false,

    rollupOptions: {
      output: {
        assetFileNames: 'clientlib-vite/resources/[ext]/[name][extname]',
        chunkFileNames: 'clientlib-vite/resources/js/[name].[hash].js',
        entryFileNames: 'clientlib-vite/resources/js/[name].js',
      },
      input: {
        app: 'src/aem-vite.ts',
      },
    },
  },
  plugins: [
    vue(),
    // ... all other plugins before, 'aemViteImportRewriter' must be last
    {
      ...aemViteImportRewriter({
        publicPath: '/etc.clientlibs/aem-vite-demo/clientlibs/',
      }),

      apply: 'build',
      enforce: 'pre',
    },
  ],
}));
