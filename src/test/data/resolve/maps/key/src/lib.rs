use std::collections::HashMap;

struct MyRonStruct {
    foo: u32,
    bar: HashMap<MyInnerStruct, u32>,
}

struct MyInnerStruct {
    <ref>foo: u32,
}