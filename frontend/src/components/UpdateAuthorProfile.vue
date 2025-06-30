<template>

    <v-card outlined>
        <v-card-title>
            UpdateAuthorProfile
        </v-card-title>

        <v-card-text>
            <Number label="AuthorId" v-model="value.authorId" :editMode="editMode"/>
            <String label="Profile" v-model="value.profile" :editMode="editMode"/>
            <String label="Portfolio" v-model="value.portfolio" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="updateAuthorProfile"
            >
                UpdateAuthorProfile
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
    name: 'UpdateAuthorProfileCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.authorId = 0;
        this.value.profile = '';
        this.value.portfolio = '';
    },
    watch: {
    },
    methods: {
        updateAuthorProfile() {
            this.$emit('updateAuthorProfile', this.value);
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

