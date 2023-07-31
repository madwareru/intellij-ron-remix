struct MyRonStruct {
    <ref>foo: u32,
    baz: String,
}

struct InnerGeneric<T> {
    bar: T,
    foo: u32,
}

struct GenericStruct<T> {
    bar: InnerGeneric<T>,
    foo: u32,
}

struct OuterStruct {
    inner: GenericStruct<InnerGeneric<MyRonStruct>>,
    foo: u32,
}