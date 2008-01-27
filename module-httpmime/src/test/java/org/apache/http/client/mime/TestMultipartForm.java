/*
 * $HeadURL:$
 * $Revision:$
 * $Date:$
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.client.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.james.mime4j.field.Field;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Header;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;

public class TestMultipartForm extends TestCase {

    // ------------------------------------------------------------ Constructor
    public TestMultipartForm(final String testName) throws IOException {
        super(testName);
    }

    // ------------------------------------------------------------------- Main
    public static void main(String args[]) {
        String[] testCaseName = { TestMultipartForm.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    // ------------------------------------------------------- TestCase Methods

    public static Test suite() {
        return new TestSuite(TestMultipartForm.class);
    }

    public void testMultipartFormContent() throws Exception {
        Message message = new Message();
        Header header = new Header();
        header.addField(
                Field.parse("Content-Type: multipart/form-data; boundary=foo"));
        message.setHeader(header);
        
        Multipart multipart = new HttpMultipart();
        multipart.setParent(message);
        BodyPart p1 = new BodyPart();
        Header h1 = new Header();
        h1.addField(Field.parse("Content-Type: text/plain"));
        p1.setHeader(h1);
        p1.setBody(new StringPart("this stuff"));
        BodyPart p2 = new BodyPart();
        Header h2 = new Header();
        h2.addField(Field.parse("Content-Type: text/plain"));
        p2.setHeader(h2);
        p2.setBody(new StringPart("that stuff"));
        BodyPart p3 = new BodyPart();
        Header h3 = new Header();
        h3.addField(Field.parse("Content-Type: text/plain"));
        p3.setHeader(h3);
        p3.setBody(new StringPart("all kind of stuff"));

        multipart.addBodyPart(p1);
        multipart.addBodyPart(p2);
        multipart.addBodyPart(p3);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        multipart.writeTo(out);
        out.close();
        
        String expected = "\r\n" + 
            "--foo\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "this stuff\r\n" +
            "--foo\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "that stuff\r\n" +
            "--foo\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "all kind of stuff\r\n" +
            "--foo--\r\n" +
            "\r\n";
        String s = out.toString("US-ASCII");
        assertEquals(expected, s);
    }
    
}