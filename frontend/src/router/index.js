import { createRouter, createWebHistory } from 'vue-router'

// 페이지 컴포넌트 불러오기
import WriterDashboard from '@/views/WriterDashboard.vue'
import WriteContent from '@/views/WriteContent.vue'
import AiPreview from '@/views/AiPreview.vue'
import SubscriberDashboard from '@/views/SubscriberDashboard.vue'
import ReadBook from '@/views/ReadBook.vue'

const routes = [
  {
    path: '/',
    redirect: '/writer'  // 기본 진입 경로는 작가 대시보드
  },
  {
    path: '/writer',
    name: 'WriterDashboard',
    component: WriterDashboard
  },
  {
    path: '/writer/write',
    name: 'WriteContent',
    component: WriteContent
  },
  {
    path: '/ai-preview',
    name: 'AiPreview',
    component: AiPreview
  },
  {
    path: '/subscriber',
    name: 'SubscriberDashboard',
    component: SubscriberDashboard
  },
  {
    path: '/subscriber/read',
    name: 'ReadBook',
    component: ReadBook
  },
  {
  path: '/writer/edit',
  name: 'EditContent',
  component: () => import('@/views/EditContent.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
