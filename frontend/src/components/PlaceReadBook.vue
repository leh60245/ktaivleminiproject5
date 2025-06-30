<template>

    <v-card outlined>
        <v-card-title>
            PlaceReadBook
        </v-card-title>

        <v-card-text>
            <Number label="UserId" v-model="value.userId" :editMode="editMode"/>
            <Number label="BookId" v-model="value.bookId" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="placeReadBook"
            >
                PlaceReadBook
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'PlaceReadBookCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.userId = 0;
        this.value.bookId = 0;
    },
    watch: {
    },
    methods: {
        placeReadBook() {
            this.$emit('placeReadBook', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

