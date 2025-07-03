<template>
  <section class="pa-6">
    <h2 class="text-h4 mb-6">📚 전체 도서 보기</h2>

    <v-row dense>
      <v-col
        v-for="(book, index) in books"
        :key="index"
        cols="12" sm="6" md="4" lg="3"
      >
        <v-card
          class="pa-2"
          elevation="2"
          @click="goToDetail(book)"
          style="cursor: pointer;"
        >
          <v-img
            :src="book.coverImageUrl || defaultImage"
            height="200"
            cover
            class="mb-2"
          />
          <v-card-title class="text-h6">{{ book.title }}</v-card-title>
          <v-card-subtitle class="text-subtitle-2">
            👤 {{ book.author || '미상' }}
          </v-card-subtitle>
          <v-card-text class="text-truncate">
            {{ book.summary || '요약이 없습니다.' }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <p v-if="!books.length" class="text-grey mt-4">출간된 책이 없습니다.</p>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const books = ref([])
const defaultImage = '/default-cover.png'

function goToDetail(book) {
  router.push({ name: 'BookDetail', params: { title: book.title } })
}

onMounted(() => {
  const stored = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
  books.value = stored.filter(book => book.status === 'published')
})
</script>

<style scoped>
.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
