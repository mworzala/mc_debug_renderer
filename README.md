> I have no plans to reliably update this mod, and it is not guaranteed to work perfectly (for example debug shapes are not cleared when disconnecting currently)

# Minecraft Debug Renderer

Allows servers to render arbitrarily placed and sized debug shapes on the client.

At the moment, the following plugin message is supported:



## Plugin Messages
The following plugin messages are supported:

### Multi Operation
<table>
<thead>
  <tr>
    <th>PMC ID</th>
    <th>Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="2">debug:shapes</td>
    <td>Op Count</td>
    <td>VarInt</td>
    <td>The number of operations following</td>
  </tr>
  <tr>
    <td>Operation</td>
    <td>Operation</td>
    <td>See below</td>
  </tr>
</tbody>
</table>

### Single Operation

<table>
<thead>
  <tr>
    <th>PMC ID</th>
    <th colspan="2">Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="7">debug_shape</td>
    <td colspan="2">Type</td>
    <td>NamespaceID</td>
    <td></td>
  </tr>
  <tr>
    <td rowspan="3">SET (0)</td>
    <td>ID</td>
    <td>NamespaceID</td>
    <td>The ID of the shape (can be anything, conflicts will be replaced)</td>
  </tr>
  <tr>
    <td>Shape Type</td>
    <td>VarInt</td>
    <td>See below</td>
  </tr>
  <tr>
    <td>Shape</td>
    <td>Shape</td>
    <td>See below</td>
  </tr>
  <tr>
    <td>REMOVE (1)</td>
    <td>Target</td>
    <td>NamespaceID</td>
    <td></td>
  </tr>
  <tr>
    <td>CLEAR_NS (2)</td>
    <td>Target</td>
    <td>String</td>
    <td>Clears all entries in the given namespace.</td>
  </tr>
  <tr>
    <td>CLEAR (3)</td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
</tbody>
</table>

## Shape Types

### Box

<table>
<thead>
  <tr>
    <th>Shape ID</th>
    <th>Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="4">0</td>
    <td>Start</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td>End</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td>Color</td>
    <td>Integer</td>
    <td>in ARGB format (8 bits per component)</td>
  </tr>
  <tr>
    <td>Layer</td>
    <td>Enum[INLINE, TOP] (VarInt)</td>
    <td>INLINE mode will render behind impeding blocks, TOP will draw above everything else (eg always visible)</td>
  </tr>
</tbody>
</table>

### Line

<table>
<thead>
  <tr>
    <th>Shape ID</th>
    <th>Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="5">1</td>
    <td>Point Count</td>
    <td>VarInt</td>
    <td>The number of points following, they will be drawn as a strip</td>
  </tr>
  <tr>
    <td>Point</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td>Thickness</td>
    <td>Float</td>
    <td>Line thickness, only supported on some GPUs</td>
  </tr>
  <tr>
    <td>Color</td>
    <td>Integer</td>
    <td>in ARGB format (8 bits per component)</td>
  </tr>
  <tr>
    <td>Layer</td>
    <td>Enum[INLINE, TOP] (VarInt)</td>
    <td>INLINE mode will render behind impeding blocks, TOP will draw above everything else (eg always visible)</td>
  </tr>
</tbody>
</table>

### Text

<table>
<thead>
  <tr>
    <th>Shape ID</th>
    <th>Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="5">2</td>
    <td>Point</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td>Content</td>
    <td>String</td>
    <td>The text to render</td>
  </tr>
  <tr>
    <td>Color</td>
    <td>Integer</td>
    <td>in ARGB format (8 bits per component)</td>
  </tr>
  <tr>
    <td>Size</td>
    <td>Float</td>
    <td></td>
  </tr>
  <tr>
    <td>Layer</td>
    <td>Enum[INLINE, TOP] (VarInt)</td>
    <td>INLINE mode will render behind impeding blocks, TOP will draw above everything else (eg always visible)</td>
  </tr>
</tbody>
</table>


# License
This project is licensed under the [MIT License](./LICENSE).