use std::collections::HashMap;

struct MyRonStruct {
    foo: u32,
    bar: HashMap<u32, MyInnerStruct>,
}

struct MyInnerStruct {
    <ref>foo: u32,
}