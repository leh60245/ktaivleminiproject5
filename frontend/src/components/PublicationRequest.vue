<template>
    <div>
        <Number
            label="PublicationRequestId"
            v-model="value.publicationRequestId"
            :editMode="editMode"
        />
        <Date
            label="PublishedDate"
            v-model="value.publishedDate"
            :editMode="editMode"
        />
        <Date
            label="CreatedDate"
            v-model="value.createdDate"
            :editMode="editMode"
        />
        <String
            label="Category"
            v-model="value.category"
            :editMode="editMode"
        />
        <String
            label="Summary"
            v-model="value.summary"
            :editMode="editMode"
        />
        <String
            label="CoverImageUrl"
            v-model="value.coverImageUrl"
            :editMode="editMode"
        />
        <PublicationFormat
            offline
            label="Format"
            v-model="value.format"
            :editMode="editMode"
            @change="change"
        />
        <PublicationRequestStatus
            offline
            label="Status"
            v-model="value.status"
            :editMode="editMode"
            @change="change"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'PublicationRequest',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "publicationRequests",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
