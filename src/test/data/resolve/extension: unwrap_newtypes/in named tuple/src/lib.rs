struct MyRonStruct {
    foo: u32,
    bar: (NewType,),
}

struct NamedTuple(NewType, u32);
struct NewType(MyInner);

struct MyInner {
  <ref>foo: u32,
}