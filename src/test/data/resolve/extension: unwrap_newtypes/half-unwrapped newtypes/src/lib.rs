struct MyRonStruct {
    foo: u32,
    bar: OuterNewType,
}

struct OuterNewType(InnerNewType);
struct InnerNewType(MyInner);

struct MyInner {
  <ref>foo: u32,
}