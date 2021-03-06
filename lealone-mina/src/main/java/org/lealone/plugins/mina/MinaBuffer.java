/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.plugins.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.lealone.net.NetBuffer;

public class MinaBuffer implements NetBuffer {

    private final IoBuffer buffer;

    public MinaBuffer(IoBuffer buff) {
        this.buffer = buff;
    }

    public IoBuffer getBuffer() {
        return buffer;
    }

    @Override
    public MinaBuffer appendBuffer(NetBuffer buff) {
        if (buff instanceof MinaBuffer) {
            int pos = buffer.position();
            buffer.position(buffer.limit());
            buffer.put(((MinaBuffer) buff).getBuffer());
            buffer.flip();
            buffer.position(pos);
        }
        return this;
    }

    @Override
    public int length() {
        int pos = buffer.position();
        if (pos > 0)
            return pos;
        else
            return buffer.limit();
    }

    @Override
    public MinaBuffer slice(int start, int end) {
        return new MinaBuffer(buffer.getSlice(start, end - start));
    }

    @Override
    public MinaBuffer getBuffer(int start, int end) {
        byte[] bytes = new byte[end - start];
        // 不能直接这样用，get的javadoc是错的，start应该是bytes的位置
        // buffer.get(bytes, start, end - start);
        int pos = buffer.position();
        buffer.position(start);
        buffer.get(bytes, 0, end - start);
        buffer.position(pos);
        IoBuffer newBuffer = IoBuffer.wrap(bytes);
        newBuffer.setAutoExpand(true);
        return new MinaBuffer(newBuffer);
    }

    @Override
    public short getUnsignedByte(int pos) {
        return buffer.getUnsigned(pos);
    }

    @Override
    public MinaBuffer appendByte(byte b) {
        buffer.put(b);
        return this;
    }

    @Override
    public MinaBuffer appendBytes(byte[] bytes, int offset, int len) {
        buffer.put(bytes, offset, len);
        return this;
    }

    @Override
    public MinaBuffer appendInt(int i) {
        buffer.putInt(i);
        return this;
    }

    @Override
    public MinaBuffer setByte(int pos, byte b) {
        buffer.put(pos, b);
        return this;
    }
}
