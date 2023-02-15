struct MyRonStruct {
    foo: u32,
    bar: &'static [MyInnerStruct],
}

struct MyInnerStruct {
    <ref>foo: u32,
}