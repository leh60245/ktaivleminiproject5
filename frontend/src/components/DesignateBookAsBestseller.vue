<template>

    <v-card outlined>
        <v-card-title>
            DesignateBookAsBestseller
        </v-card-title>

        <v-card-text>
            <Number label="BookId" v-model="value.bookId" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="designateBookAsBestseller"
            >
                DesignateBookAsBestseller
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
    name: 'DesignateBookAsBestsellerCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.bookId = 0;
    },
    watch: {
    },
    methods: {
        designateBookAsBestseller() {
            this.$emit('designateBookAsBestseller', this.value);
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

