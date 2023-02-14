mod a {
    struct OuterStruct {
        my: MyRonStruct,
    }
    struct <ref>MyRonStruct {
        foo: u32,
        bar: String,
    }
}

mod b {
    struct MyRonStruct {
        foo: u32,
        bar: String,
    }
}