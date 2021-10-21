import './aem-vite-styles.css';
import { createApp } from 'vue';
import Counter from './components/counter/Counter.vue';

// const helloClass = new Hello();
// helloClass.hello();
//
// console.log('>>> aem-vite here!');

const counterElement = document.querySelector('counter');

if (counterElement) {
  createApp(Counter, { ...(counterElement as HTMLElement).dataset }).mount(
    'counter'
  );
}
