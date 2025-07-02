<template>
  <div class="pa-8" style="background-color: #f5f5f5; min-height: 100vh;">
    <h2 class="mb-8">✏️ 글 수정</h2>

    <v-text-field v-model="title" label="제목" outlined dense class="mb-6" style="background-color: white;" />
    <v-textarea v-model="content" label="본문 내용" rows="10" outlined dense class="mb-8" style="background-color: white;" />

    <v-btn color="primary" @click="saveEdit">수정 완료</v-btn>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const id = ref(null)
const title = ref('')
const content = ref('')

onMounted(() => {
  const q = route.query
  id.value = Number(q.id)
  const posts = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
  const target = posts.find(p => p.id === id.value)

  if (target) {
    title.value = target.title
    content.value = target.content
  } else {
    alert('글을 찾을 수 없습니다.')
    router.push('/writer')
  }
})

function saveEdit() {
  const posts = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
  const updated = posts.map(post => {
    if (post.id === id.value) {
      return { ...post, title: title.value, content: content.value }
    }
    return post
  })

  localStorage.setItem('writtenPosts', JSON.stringify(updated))
  alert('수정 완료!')
  router.push('/writer')
}
</script>
