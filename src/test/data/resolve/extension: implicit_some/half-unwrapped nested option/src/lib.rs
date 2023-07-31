struct MyRonStruct {
    foo: u32,
    bar: Option<Option<MyInner>>,
}

struct MyInner {
  <ref>foo: u32,
}