struct MyRonStruct {
    foo: u32,
    bar: (MyInnerStruct, u32),
}

struct MyInnerStruct {
    <ref>foo: u32,
}