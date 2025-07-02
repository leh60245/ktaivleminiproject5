<script setup>
import { ref, onMounted } from 'vue'

const books = ref([])

function loadPosts() {
  books.value = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
}

function deletePost(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return

  const updated = books.value.filter(post => post.id !== id)
  localStorage.setItem('writtenPosts', JSON.stringify(updated))
  books.value = updated
}

onMounted(() => {
  loadPosts()
})
</script>
<template>
  <div class="pa-6">
    <h2>📚 작가 대시보드</h2>

    <v-btn color="primary" @click="$router.push('/writer/write')">
      ✍ 글 작성하러 가기
    </v-btn>

    <v-divider class="my-5" />

    <v-card v-if="books.length">
      <v-card-title>작성한 글 목록</v-card-title>
      <v-card-text>
        <ul>
            <li v-for="book in books" :key="book.id" class="mb-4">
                <div class="d-flex justify-space-between align-center">
                <div>
                    <strong>{{ book.title }}</strong> - {{ book.date }}
                </div>

                <div class="d-flex gap-2">
                    <v-btn size="small" color="warning" @click="$router.push({ path: '/writer/edit', query: { id: book.id } })">
                    수정
                    </v-btn>

                    <v-btn size="small" color="error" @click="deletePost(book.id)">
                    삭제
                    </v-btn>
                </div>
                </div>
            </li>
            </ul>
      </v-card-text>
    </v-card>
    <p v-else>작성한 글이 아직 없습니다.</p>
  </div>
</template>
