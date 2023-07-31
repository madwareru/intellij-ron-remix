struct MyRonStruct {
    <ref>foo: u32,
    baz: String,
}

struct GenericStruct<T> {
    bar: T,
    foo: u32,
}

struct OuterStruct {
    inner: GenericStruct<MyRonStruct>,
    foo: u32,
}