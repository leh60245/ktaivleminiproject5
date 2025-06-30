import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/authors',
      component: () => import('../components/ui/AuthorGrid.vue'),
    },
    {
      path: '/subscriptions',
      component: () => import('../components/ui/SubscriptionGrid.vue'),
    },
    {
      path: '/books',
      component: () => import('../components/ui/BookGrid.vue'),
    },
    {
      path: '/users',
      component: () => import('../components/ui/UserGrid.vue'),
    },
    {
      path: '/manuscripts',
      component: () => import('../components/ui/ManuscriptGrid.vue'),
    },
    {
      path: '/publicationRequests',
      component: () => import('../components/ui/PublicationRequestGrid.vue'),
    },
  ],
})

export default router;
