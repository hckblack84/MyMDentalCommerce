module.exports = function (babel) {
  const { types: t } = babel;

  return {
    visitor: {
      MemberExpression(path) {
        const { node } = path;

        if (
          node.object?.type === "MemberExpression" &&
          node.object.object?.type === "MetaProperty" &&
          node.object.object.property?.name === "meta" &&
          node.object.property?.name === "env"
        ) {
          path.replaceWith(
            t.memberExpression(
              t.memberExpression(
                t.identifier("process"),
                t.identifier("env")
              ),
              t.identifier(node.property.name)
            )
          );
          return;
        }

        if (
          node.property?.name === "env" &&
          node.object?.type === "MetaProperty" &&
          node.object.property?.name === "meta"
        ) {
          const parent = path.parentPath;
          if (parent.isMemberExpression() && parent.node.object === node) {
            return;
          }
          path.replaceWith(
            t.memberExpression(
              t.identifier("process"),
              t.identifier("env")
            )
          );
        }
      }
    }
  };
};
