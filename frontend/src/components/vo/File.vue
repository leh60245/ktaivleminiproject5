<template>
    <div style="margin: 0 -15px 0 -15px;">
        <v-card-title>
            {{label}}
        </v-card-title>
        <v-card-text v-if="value">
            <div v-if="editMode" style="margin-top:-20px;">
                <v-file-input label="File input" v-model="file" @change="change"/>
            </div>
            <div v-else style="cursor: pointer;">
                <div @click="download">
                    <v-icon text>mdi-paperclip</v-icon>
                    <span class="mx-2 text-decoration-underline">
                        {{ value.fileName }}
                    </span>
                </div>
            </div>
        </v-card-text>
    </div>
</template>

<script>
    export default {
        name: "File",
        props: {
            editMode: Boolean,
            value : [Object, String, Number, Array],
            label : String,
        },
        data: () => ({
            file: null,
        }),
        created(){
            if(!this.value.file) {
                this.value = {
                    "fileName": "",
                    "file": null,
                };
            } else {
                this.file = new File([this.value.file], this.value.fileName);
            }
        },
        methods: {
            change(){
                var me = this;
                var reader = new FileReader();
                reader.onload = function () {
                    var result = reader.result;
                    this.file = result;
                    var newValue = {
                        "file": result,
                        "fileName": me.value.fileName
                    }
                    me.$emit("update:modelValue", newValue);
                };
                reader.readAsDataURL( this.file );
                this.value.fileName = this.file.name

            },
            download() {
                var link = document.createElement('a');
                link.href = this.file;
                link.setAttribute('download', this.value.fileName);
                document.body.appendChild(link);
                link.click();
            }
        },
    }
</script>