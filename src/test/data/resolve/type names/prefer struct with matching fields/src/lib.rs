
mod a {
    struct MyRonStruct {
        foo: u32,
        baz: String,
    }
}

mod b {
    struct <ref>MyRonStruct {
        foo: u32,
        bar: String,
    }
}