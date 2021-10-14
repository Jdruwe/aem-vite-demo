import './styles.css';

import { createApp } from 'vue';
import Counter from './components/counter/Counter.vue';

const counterElement = document.querySelector('counter');

if (counterElement) {
  createApp(Counter, { ...(counterElement as HTMLElement).dataset }).mount(
    'counter'
  );
}
